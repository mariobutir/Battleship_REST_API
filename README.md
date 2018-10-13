# Mario Butir - Battleship assignment

REST API backend for Battleship

## Build and deploy docker image to openshift.ag04.io

### Login to openshift and registry

Use interactive login:
```
$ oc login https://openshift.ag04.io
```
Once you are logged in in oc, issue the following command to log into registry:

```
$ docker login -p $(oc whoami -t) -u unused registry.os.ag04.io
```

Select target project in openshift instance (development, demo, production)

Build and deploy version 0.2.0.

```
./gradlew clean build
./gradlew buildDocker
docker tag ag04/mb-battleship-api:0.2.0 registry.os.ag04.io/development/mb-battleship-api:latest
docker push registry.os.ag04.io/development/mb-battleship-api:latest
```