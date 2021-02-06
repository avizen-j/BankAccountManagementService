# Bank account management API

## How to build and run?
1. Navigate to application root directory (docker-compose.yml will be there)
2. Run `./mvnw clean package -DskipTests`
3. Run `docker-compose up -d`

## Example file structure
| accountNumber | operationDate       | beneficiary | comment                 | amount | currency |
|---|---|---|---|---|---|
| 440924706     | 2020-10-28 03:24:24 | Mora        | Phased global product   | 88     | UAH      |
| 519329317     | 2020-03-28 8:15:24  | Leoline     | Reduced modular synergy | 33.2   | EUR      |

Valid example .csv file could be downloaded from https://easyupload.io/nkvt59

## Data types
- accountNumber [mandatory] - string
- operationDate [mandatory] - ISO 8601 date time
- beneficiary[mandatory] - string
- comment[optional] - string
- amount[mandatory] - double
- currency[mandatory] - string
