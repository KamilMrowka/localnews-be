# What does this application do?

LocalNews AI is a backend application that matches news articles to US cities using AI.  
By analyzing articles with OpenAI's API, it classifies them as either local or global and links them to relevant cities.  
It powers [this frontend](https://github.com/KamilMrowka/localnews-fe.git).

## Required ENV variables

```
DB_URL
POSTGRES_USER
POSTGRES_PASSWORD
POSTGRES_DB
OPEN_AI_SECRET_KEY - get from OpenAI dashboard
MIGRATION_SECRET_KEY - create this key manually, it's for accessing migration endpoints
```

## Usage

1. Create a .env file and start the application.
2. Call `api/v1/migrate/load-json` and `api/v1/migrate/load-csv` **`GET`** endpoints with a `MIGRATION_KEY_HEADER` set to the same value as the migration secret key specified in the .env file.
3. Call the `api/v1/migrate/migrate-new-articles` **`GET`** endpoint (use same header as previously).

You can now access cities by calling the `api/v1/cities` **`GET`** endpoint with a `query` string. It returns a list of City objects that match the `query`.

To find articles for a chosen city call the `api/v1/news/local` endpoint with `city_id` and `page` params.

## How does it work?

The application has two static sets of articles.
Both were scraped with a python scraper made specifically for this project.  
It can be found [here](https://github.com/KamilMrowka/news_scrapers.git).

When calling some variation of the `migrate-articles` endpoint  
each article is first classified as either `global` or `local`,  
and (if possible) local articles are attached to a city from a static dataset.

Both these actions happen in the same request to **OpenAI's API**.  
Articles are processed in batches.

Everything is stored in a **PostgreSQL** database and easily accessed via `api/v1/news/local` **`GET`** endpoint.
All endpoints except for migration endpoints are publicly accessible as they are not sensitive.

## Tech Stack
- Java
- Spring Boot
- PostgreSQL
- OpenAI's API
