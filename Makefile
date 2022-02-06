# build image
build:
	docker build -t scala-sandbox:latest .

# start container to sbt shell
start:
	docker run --rm -i scala-sandbox:latest sbt

# run sbt test
test:
	docker run --rm -i scala-sandbox:latest sbt clean test