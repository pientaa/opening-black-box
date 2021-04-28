# Run database

## Get data

```
chmod 755 get_data.sh
./get_data.sh
```

## Run database
Should be run on a separate node
```
chmod 755 run_database.sh
./run_database.sh
```

# Setup TPC-DS

## On local machine

### Install required packages
```
sudo apt install gcc make flex bison unzip
```

### Download TPC-DS

1. Go to [TPC Downloads](http://tpc.org/tpc_documents_current_versions/current_specifications5.asp)
2. Choose a version to download.
3. Register free account to get TPC tool.
4. Check mail for a link to download .
   __The file can be downloaded only once!__
5. Download the file to `database` directory.

### Unzip the file

```
unzip <file name> -d tpc-ds
```

### Change `makefile`
Go to `tools` directory
```
cd tpc-ds/<version_dir>/tools
```
Copy `Makefile.suite` to `makefile`
```
cp Makefile.suite makefile
```
Edit `makefile` line containing "OS = ". 
For example: "OS = LINUX".

## Compile binaries
```
make
```

## Generate data locally
The data is generated via `dsdgen`. 
The data size can be specified with `-SCALE` option.
The output directory (specified with `-DIR` option) must exist prior to running 
   
Below commands create a directory `database/data` and generate 1 GB of data.
Run it in `tools` directory.

```
mkdir ./../../../data
./dsdgen -SCALE 1 -DIR ./../../../data
```

## Copy schema file
`tools/tpcds.sql` file contains the schema of the generated data, which can be used to create tables in a database.

```
cp tpcds.sql ./../../..
```

## On a remote machine (via ssh)

In working directory (`opening-black-box`) run below command.

```bash
database/prepare_tpc_ds.sh
```