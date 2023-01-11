Linkerd(helm threw some error in pod crashloopbackoff):
curl --proto '=https' --tlsv1.2 -sSfL https://run.linkerd.io/install | sh
export PATH=$PATH:/home/jagansingh/.linkerd2/bin
linkerd check --pre
linkerd install --crds | kubectl apply -f -
linkerd install --set proxyInit.runAsRoot=true | kubectl apply -f -
linkerd check
linkerd viz install | kubectl apply -f
linkerd check
linkerd viz dashboard &


Infra:
helm repo add elastic https://helm.elastic.co
helm repo update

ECK
helm install elastic-operator elastic/eck-operator -n elastic-system --create-namespace

Cluster
kubectl apply -f ./switchin/event-api/chart/other/elastic.yaml -n event
kubectl apply -f ./switchin/event-api/chart/other/kibana.yaml -n event
kubectl expose service quickstart-kb-http --port=5601 --target-port=5601 --name=quickstart-kb-http-ext --type=NodePort -n event
Optional: kubectl expose service quickstart-es-http --port=9200 --target-port=9200 --name=quickstart-es-http-ext --type=NodePort -n event
echo $(kubectl get secret -n event quickstart-es-elastic-user -o go-template='{{.data.elastic | base64decode}}')

minikube service list
minikube service quickstart-kb-http-ext

Application:
helm uninstall event-api -n event
helm install event-api ./switchin/event-api/chart/event-api/ --values ./switchin/event-api/chart/event-api/values.yaml -n event
OR
helm upgrade event-api ./switchin/event-api/chartevent-api/ --values event-api/values.yaml -n event

minikube service event-api

Follow:
https://www.elastic.co/guide/en/cloud-on-k8s/current/k8s-deploy-eck.html
https://www.elastic.co/guide/en/cloud-on-k8s/current/k8s-stack-helm-chart.html



Remove:
helm uninstall event-api -n event
kubectl delete -f ./switchin/event-api/chart/other/elastic.yaml -n event
kubectl delete -f ./switchin/event-api/chart/other/kibana.yaml -n event
kubectl delete service quickstart-kb-http-ext -n event


