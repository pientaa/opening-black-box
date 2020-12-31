# opening-black-box


## Get data

In order to get the data go to `black-box/database` and run `get_data.sh`.

### [UPDATE] 
`start.sh` will build artifact and start containers for both db and black-box

### Run

In order to run a `black-box`, build artifact (it should be placed in: `opening-black-box/black-box/target/scala-2.11/`) - it is suggested to use `sbt assembly`, 
go to `opening-black-box/black-box` directory and run `start.sh` or `docker-compose up`.
Difference is that script will additionally write stream of `docker stats` to file `mystats.csv`. 

For now, we don't use that metrics, but it's purpose is to show that such metrics are easy to collect when your app is dockerised.

Data that is used for the project you can download from google drive with following [link](https://drive.google.com/drive/folders/1rs4D6KzBvZCxSthDeIPzbyZRVHMQwOfI?usp=sharing)
