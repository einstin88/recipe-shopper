import time
import json

from bs4 import BeautifulSoup
from chromedriver_py import binary_path
from selenium import webdriver
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service
import jsonpickle

from .constants import *
from .models.product import Product
from .utils import price_in_float


WEBPAGE_LOAD_TIME_LIMIT = 5
WEBSITE_SCROLL_LIMIT = 2


class Scrapper:
    '''
    Class that implements functions to load web pages and clean the data
    '''

    def __init__(self, url: str) -> None:
        self.url = url
        self.products_html = ''

    def fetch_html(self) -> None:
        '''
        Use selenium to load and scroll the page before saving the loaded html
        '''
        # Set options of selenium webdriver
        options = Options()
        options.add_argument('--headless')
        # options.add_argument('--start-maximized')
        # options.add_argument('--incognito')
        options.add_argument('--no-sandbox')
        options.add_argument('--disable-dev-shm-usage')
        options.add_argument('--blink-settings=imagesEnabled=false')

        # Set binary path of the chrome webdriver
        service = Service(binary_path=binary_path)

        # Instantiate a chrome webdriver to load contents from the url
        browser = webdriver.Chrome(service=service, options=options)
        browser.implicitly_wait(WEBPAGE_LOAD_TIME_LIMIT)

        print(f'>>> Loading html from source: {self.url}\n')
        browser.get(self.url)

        # Scrolls to bottom of the page
        footer = browser.find_element(By.CLASS_NAME, SS_PAGE_FOOTER)
        for _ in range(WEBSITE_SCROLL_LIMIT):
            ActionChains(browser).scroll_to_element(footer).perform()
            time.sleep(WEBPAGE_LOAD_TIME_LIMIT + 2)

        # Saves the raw html in memory for further processing
        self.products_html = browser.page_source
        # print(f'>>> HTML: {self.products_html}\n')
        browser.quit()

    def parse_for_products(self) -> list[dict]:
        '''        
        1. Load contents of the url page that need to be parsed
        2. Get the div of product list
        3. Split products by their 'div's

        Returns: list of products as dict 
        '''
        print(f'>>> Parsing html for product details...')

        products_list = BeautifulSoup(self.products_html, 'lxml').find(
            'div', class_=SS_PRODUCT_CONTAINER
        ).find_all(class_=SS_PRODUCT_CARD)

        products: list[dict] = []
        for product in products_list:
            product_soup = BeautifulSoup(str(product), 'lxml')

            product_url = product['href']
            name = product_soup.find(class_=SS_PRODUCT_NAME).text.strip()
            unit = product_soup.find(class_=SS_PRODUCT_UNIT).text.strip()
            price = price_in_float(
                product_soup.find(
                    class_=SS_PRODUCT_PRICE).span.text.strip())

            try:
                img_url = product_soup.find(class_=SS_PRODUCT_IMG)[
                    'src'].strip()
            except (AttributeError, TypeError):
                img_url = ''

            product_to_add = json.loads(
                jsonpickle.encode(
                    Product(name,
                            product_url,
                            unit,
                            price,
                            img_url)))
            
            # print(f'>>> Product parsed: {product_to_add}')
            products.append(product_to_add)

        return products

    def save_html(self, html_file: str) -> None:
        '''
        Sets the html to scrape that is gotten via FileUpload
        '''
        self.products_html = html_file

    def save_to_file(self, file_path: str) -> None:
        '''
        EXPERIMENTAL
        '''
        with open(file_path, 'w') as f:
            f.writelines(self.products_html)
