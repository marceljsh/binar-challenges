all: tidy build run

build:
	javac -d ./bin ./src/challenge01/app/*.java ./src/challenge01/driver/*.java ./src/challenge01/helper/*.java ./src/challenge01/model/entity/*.java ./src/challenge01/model/repository/*.java

tidy:
	rm -rf ./bin/*
	rm -rf ./output/*

run:
	java -cp ./bin challenge01.driver.Driver
