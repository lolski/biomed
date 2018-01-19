# Biomed Proof-Of-Concept Application
This repository contains the Biomed proof-of-concept application. The application is designed to showcase Grakn's schema, query language, reasoning, and data migration feature. It mainly does two things:
1. Migrating data from various sources (mainly .csv and .gql files) into Grakn.
2. Performing various queries, showing the power of the query language, Graql.

# Prerequisites
1. Make sure to install the Python-related prerequisites - `pip`, `pyenv`, Python 3, and `virtualenv`:
```
$ sudo easy_install pip
$ brew install pyenv
$ pyenv install 3.6.3
$ pip install --upgrade virtualenv
```

2. Activate `virtualenv` with Python 3 in `<path-to-biomed-poc-directory>`:
```
$ pyenv use 3.6.3
$ eval "$(pyenv init -)"
$ cd <path-to-biomed-poc-directory>
$ virtualenv --system-site-packages -p python3 .
```

# Running The Application
## 1. Migrating Data Into Grakn
1. Start Grakn: `cd <path-to-grakn-directory> && ./grakn server start`
2. Run the following command, supplying the Grakn directory. The data will be loaded in a keyspace named `biomed`
```
$ cd <path-to-biomed-poc-directory>
$ source bin/activate
$ pip install -r requirements.txt
$ ./load_to_graql.sh <path-to-grakn-directory>
```

## Performing Queries
Have a look at file `biomed_queries.gql` for list of queries often used for demo purpose. There are several places where you can perform the queries. They can be ran from:
1. The web dashboard (`http://localhost:4567`)
2. Graql console (`graql console -k <keyspace>`)
3. The Grakn Workbase application.

NOTE: Most of the queries are old, e.g., `match` instead of `match x; get;`. This means that it won't work on Grakn 1.0 onwards.
