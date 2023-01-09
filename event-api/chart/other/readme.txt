Infra:
helm repo add elastic https://helm.elastic.co
helm repo update

ECK
helm install elastic-operator elastic/eck-operator -n elastic-system --create-namespace

Cluster
kubectl apply -f .\other\elastic.yaml
kubectl apply -f .\other\kibana.yaml
kubectl expose service quickstart-kb-http --port=5601 --target-port=5601 --name=quickstart-kb-http-ext --type=NodePort
Optional: kubectl expose service quickstart-es-http --port=9200 --target-port=9200 --name=quickstart-es-http-ext --type=NodePort
echo $(kubectl get secret quickstart-es-elastic-user -o go-template='{{.data.elastic | base64decode}}')

minikube service list
minikube service quickstart-kb-http-ext

Application:
helm uninstall event-api
helm install event-api event-api/ --values event-api/values.yaml
OR
helm upgrade event-api event-api/ --values event-api/values.yaml

minikube service event-api

Follow:
https://www.elastic.co/guide/en/cloud-on-k8s/current/k8s-deploy-eck.html
https://www.elastic.co/guide/en/cloud-on-k8s/current/k8s-stack-helm-chart.html
