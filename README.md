# Monkey

## Introduction
Monkey is a lossless compression algorithm for floating-point, including 32-bit and 64-bit.

## Comparation with Gorilla
Monkey has better compression ratio than Gorilla.

Monkey algorithm is inspired
by ["Gorilla: A Fast, Scalable, In-Memory Time Series Database"](http://www.vldb.org/pvldb/vol8/p1816-teller.pdf).
Gorilla has been wildly used in TimeSeries DBMS such as InfluxDB and Apache IoTDB.


Table: 64-bit floating-point compression comparison
| File Name | Monkey Size | Grilla Size | **Monkey/Gorilla** |
|------------------------------------------------|-------------|-------------|---------------------|
| Beach_Water_Quality_-_Automated_Sensors.csv | 787352 | 878544 | 0.896200987087727 |
| stream-gages-1.csv | 6452072 | 7672992 | 0.8408808454381289 |
| stream-gages.csv | 3973608 | 4243320 | 0.9364384491388819 |
| Beach_Weather_Stations_-_Automated_Sensors.csv | 4266648 | 5142088 | 0.829750093736241 |

Test data is from https://data.cityofchicago.org/.

The Java base implementation is inspired by [gorilla-tsc](https://github.com/burmanm/gorilla-tsc).

## Test your data:
1. Make a csv file, one float/double per line;
2. Put the csv file in folder src/test/resources/data;
3. Run test: SizeBenchmark.java
