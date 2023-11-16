# Monkey

Monkey is a compression method that inspired
by ["Gorilla: A Fast, Scalable, In-Memory Time Series Database"](http://www.vldb.org/pvldb/vol8/p1816-teller.pdf).

Monkey is focus on floating-point compression, including 32-bit and 64-bit.

Normally, Monkey has better compression ratio than Gorilla.

Table: 64-bit floating-point compression.
| File Name                                      | Monkey size | Grilla Size | Monkey/Gorilla      |
|------------------------------------------------|-------------|-------------|---------------------|
| Beach_Water_Quality_-_Automated_Sensors.csv    | 787352      | 878544	     | 0.896200987087727   |
| stream-gages-1.csv                             | 	6452072    | 	7672992	   | 0.8408808454381289  |
| stream-gages.csv                               | 	3973608	   | 4243320     | 	0.9364384491388819 |
| Beach_Weather_Stations_-_Automated_Sensors.csv | 	4266648    | 	5142088    | 	0.829750093736241  |

Test data is from https://data.cityofchicago.org/.
The Java base implementation is inspired by [gorilla-tsc](https://github.com/burmanm/gorilla-tsc).
