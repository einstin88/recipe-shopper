# Getting Started
1. Create virtual environment for python with `venv <dir>` or preferably `virtualenv <dir>`
2. Install dependencies with `pip install -r`
3. CD to the Python server directory
4. Setup an instance of mysql and set the environment variables required by 'settings.py'. Keyword: os.getenv()
5. CD to Python server directory and `py manage.py makemigrations` then `... migrate`
6. Create Django superuser: `... createsuperuser`
7. Start the server: `... runserver`