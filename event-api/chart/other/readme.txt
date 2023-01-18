Ingress:
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
helm install ingress-nginx ingress-nginx/ingress-nginx --values ingress-controller-linkerd-inject.yaml


Linkerd(helm threw some error in pod crashloopbackoff):
curl --proto '=https' --tlsv1.2 -sSfL https://run.linkerd.io/install | sh
export PATH=$PATH:/home/jagansingh/.linkerd2/bin
linkerd check --pre
linkerd install --crds | kubectl apply -f -
linkerd install --set proxyInit.runAsRoot=true | kubectl apply -f -
linkerd check
linkerd viz install | kubectl apply -f -
linkerd check
#jaeger after vertx jaeger log4j2 configuration
#linkerd jaeger install | kubectl apply -f -
#linkerd check
#pending exploration on jaeger
helm repo add grafana https://grafana.github.io/helm-charts
helm install grafana -n grafana --create-namespace grafana/grafana -f https://raw.githubusercontent.com/linkerd/linkerd2/main/grafana/values.yaml
linkerd viz install --set grafana.url=grafana.grafana:3000 | kubectl apply -f -
kubectl apply -f ./switchin/event-api/chart/other/linkerd-dashboard-ingress.yaml
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

minikube service quickstart-kb-http-ext -n event

PUT /events




Application:
helm uninstall event-api -n event
helm install event-api ./switchin/event-api/chart/event-api/ --values ./switchin/event-api/chart/event-api/values.yaml -n event
OR
helm upgrade event-api ./switchin/event-api/chart/event-api/ --values ./switchin/event-api/chart/event-api/values.yaml -n event

minikube service list -n event
minikube service event-api -n event

minikube tunnel

Follow:
https://www.elastic.co/guide/en/cloud-on-k8s/current/k8s-deploy-eck.html
https://www.elastic.co/guide/en/cloud-on-k8s/current/k8s-stack-helm-chart.html



Remove:
helm uninstall event-api -n event
kubectl delete -f ./switchin/event-api/chart/other/elastic.yaml -n event
kubectl delete -f ./switchin/event-api/chart/other/kibana.yaml -n event
kubectl delete service quickstart-kb-http-ext -n event



Chrome:
sudo apt update && sudo apt -y upgrade && sudo apt -y autoremove
wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
sudo apt -y install ./google-chrome-stable_current_amd64.deb
google-chrome --version
google-chrome
https://scottspence.com/posts/use-chrome-in-ubuntu-wsl





helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo add elastic https://helm.elastic.co
helm repo add grafana https://grafana.github.io/helm-charts
helm repo update
curl --proto '=https' --tlsv1.2 -sSfL https://run.linkerd.io/install | sh
export PATH=$PATH:/home/jagansingh/.linkerd2/bin
linkerd check --pre
linkerd install --crds | kubectl apply -f -
linkerd install --set proxyInit.runAsRoot=true | kubectl apply -f -
linkerd check
linkerd viz install | kubectl apply -f -
linkerd check
helm install grafana -n grafana --create-namespace grafana/grafana -f https://raw.githubusercontent.com/linkerd/linkerd2/main/grafana/values.yaml
linkerd viz install --set grafana.url=grafana.grafana:3000 | kubectl apply -f -
helm install ingress-nginx ingress-nginx/ingress-nginx --values ./switchin/event-api/chart/other/ingress-controller-linkerd-inject.yaml
kubectl apply -f ./switchin/event-api/chart/other/linkerd-dashboard-ingress.yaml
helm install elastic-operator elastic/eck-operator -n elastic-system --create-namespace
kubectl create namespace event
kubectl apply -f ./switchin/event-api/chart/other/elastic.yaml -n event
kubectl apply -f ./switchin/event-api/chart/other/kibana.yaml -n event
kubectl expose service quickstart-kb-http --port=5601 --target-port=5601 --name=quickstart-kb-http-ext --type=NodePort -n event
echo $(kubectl get secret -n event quickstart-es-elastic-user -o go-template='{{.data.elastic | base64decode}}')
helm install event-api ./switchin/event-api/chart/event-api/ --values ./switchin/event-api/chart/event-api/values.yaml -n event

PUT /events

%WINDIR%\System32\drivers\etc\hosts

127.0.0.1 kubernetes.docker.internal
add belows:
api - switchin-event.api
kibana - kibana.domain.example elastic/echo $(kubectl get secret -n event quickstart-es-elastic-user -o go-template='{{.data.elastic | base64decode}}')
linkerd - dashboard.example.com   admin/admin


