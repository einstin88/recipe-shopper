# Getting Started

## Development

1. Create virtual environment for python with `venv <dir>` or preferably `virtualenv <dir>`
2. Install dependencies with `pip install -r requirements.txt`
3. CD to the Python server directory
4. Setup an instance of mysql and set the environment variables required by 'settings.py'. Keyword: os.environ
5. CD to Python server directory and `py manage.py makemigrations` then `... migrate`
6. Create Django superuser: `... createsuperuser`
7. Start the server: `... runserver`

## Deployment (W.I.P.)
1. deployment config: `railway.json`
2. database
3. gunicorn config?