#docker network create --driver bridge isolated_nw

docker run -idt \
	--name=neo4j \
    --publish=7474:7474 --publish=7687:7687 \
    --network=isolated_nw \
    --network-alias=neo4j \
    -v "$(pwd)/neo4j/data":/data \
    -v "$(pwd)/neo4j/logs":/logs \
    neo4j:3.0

sleep 10s

echo "change password"
#change password
curl -H "Authorization: Basic bmVvNGo6bmVvNGo=" -H accept:application/json -H content-type:application/json -d '{"password" : "password"}' http://localhost:7474/user/neo4j/password

#echo "put data"
#put data
#curl -H "Authorization: Basic bmVvNGo6cGFzc3dvcmQ=" -H accept:application/json -H content-type:application/json --data @body.json http://172.17.0.1:7474/db/data/transaction/commit
curl -H "Authorization: Basic bmVvNGo6cGFzc3dvcmQ=" -H accept:application/json -H content-type:application/json --data @delete.json http://172.17.0.1:7474/db/data/transaction/commit

echo "run gradle plugin"
docker run \
	--name=liquigraph \
	--network=isolated_nw \
	--rm \
	-v "$(pwd)/liquiGraph":/usr/bin/app:rw \
	niaquinto/gradle \
	testV1

curl -H "Authorization: Basic bmVvNGo6cGFzc3dvcmQ=" -H accept:application/json -H content-type:application/json --data @result.json http://172.17.0.1:7474/db/data/transaction/commit

	
echo "delete neo4j"
docker rm neo4j -f
#echo "delete liquigraph"
#docker rm liquigraph -f
#curl -user neo4j:neo4j -H accept:application/json -H content-type:application/json -d '{"password" : "neo4j"}' http://localhost:7474/user/neo4j/password
