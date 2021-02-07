# Bank account management API

## How to build and run?
1. Navigate to application root directory (docker-compose.yml will be there)
2. Run `mvn clean install`
3. Run `docker-compose up -d`

## Example file structure
| accountNumber | operationDate       | beneficiary | comment                 | amount | currency |
|---|---|---|---|---|---|
| 440924706     | 2020-10-28T03:24:24 | Mora        | Phased global product   | 88     | UAH      |
| 519329317     | 2020-03-28T18:15:24  | Leoline     | Reduced modular synergy | 33.2   | EUR      |

Valid example .csv file could be downloaded from https://easyupload.io/rols9t

## Data types
- accountNumber [mandatory] - string
- operationDate [mandatory] - ISO Local Date and Time
- beneficiary[mandatory] - string
- comment[optional] - string
- amount[mandatory] - double
- currency[mandatory] - string

## Endpoints
| HTTP method | URI path       | Parameters | description |
|---|---|---|---|
| [GET] | /api/v1/export | startDate [optional], endDate [optional] | Exports all bank statements for provided period |
| [GET] | /api/v1/calculate | accountNumber [mandatory] startDate [optional], endDate [optional] | Calculates bank account balance for provided bank account number and period |
| [POST] | /api/v1/upload | file [mandatory] | Uploads bank statements from provided `text/csv` file |
