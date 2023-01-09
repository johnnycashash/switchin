helm uninstall event-api
helm install event-api event-api/ --values event-api/values.yaml
helm upgrade event-api event-api/ --values event-api/values.yaml



Follow:
https://www.elastic.co/guide/en/cloud-on-k8s/current/k8s-deploy-eck.html
https://www.elastic.co/guide/en/cloud-on-k8s/current/k8s-stack-helm-chart.html

PASSWORD=$(kubectl get secret quickstart-es-elastic-user -o go-template='{{.data.elastic | base64decode}}')

kubectl expose service quickstart-kb-http --port=5601 --target-port=5601 --name=quickstart-kb-http-ext --type=NodePort

kubectl expose service quickstart-es-http --port=9200 --target-port=9200 --name=quickstart-es-http-ext --type=NodePort


helm repo add elastic https://helm.elastic.co
helm repo update

helm install es-kb-quickstart elastic/eck-stack -n elastic-stack --create-namespace
