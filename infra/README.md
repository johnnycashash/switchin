# Infra

### Prerequisites(Any other combination can also work)
- Windows: WSL2, Docker, Mobaxterm, Git, Intellij Idea
- WSL2 machine: Minikube(minikube start --memory 4096 --cpus 4), Kubectl, Helm, k9s, Git

### Linkerd: (helm threw some error in pod crashloopbackoff):
- curl --proto '=https' --tlsv1.2 -sSfL https://run.linkerd.io/install | sh
- export PATH=$PATH:/home/jagansingh/.linkerd2/bin
- linkerd check --pre
- linkerd install --crds | kubectl apply -f -
- linkerd install --set proxyInit.runAsRoot=true | kubectl apply -f -
- linkerd check
- linkerd viz install | kubectl apply -f -
- linkerd check
- helm repo add grafana https://grafana.github.io/helm-charts
- helm repo update
- helm install grafana -n grafana --create-namespace grafana/grafana -f https://raw.githubusercontent.com/linkerd/linkerd2/main/grafana/values.yaml
- linkerd viz install --set grafana.url=grafana.grafana:3000 | kubectl apply -f -

### Ingress with Linkerd Injected:
- helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
- helm repo update
- helm install ingress-nginx -n ingress --create-namespace ingress-nginx/ingress-nginx --values ./ingress/ingress-controller-linkerd-inject.yaml

### Linkerd Dashboard Ingress Rule:
- kubectl apply -f ./linkerd/linkerd-dashboard-ingress.yaml

### Elastic Operator Install:
- helm repo add elastic https://helm.elastic.co
- helm repo update
- helm install elastic-operator elastic/eck-operator -n elastic-system --create-namespace

### Elastic/Kibana Cluster Setup(If ImagePullBackOff error, do minikube ssh and then docker pull that image version):
- kubectl create namespace elastic
- kubectl apply -f ./elastic-kibana/elastic.yaml -n elastic
- kubectl apply -f ./elastic-kibana/kibana.yaml -n elastic
- kubectl apply -f ./elastic-kibana/kibana-ingress.yaml -n elastic

### Configs:
- Add below in file %WINDIR%\System32\drivers\etc\hosts in windows and WSL2 machine in /etc/hosts
  - 127.0.0.1 dashboard.example.com kibana.domain.example
- minikube tunnel

### Kibana url:
- kibana.domain.example 
- user=elastic
- password=echo $(kubectl get secret -n elastic quickstart-es-elastic-user -o go-template='{{.data.elastic | base64decode}}')

### Linkerd url:
- dashboard.example.com   
- user=admin
- password=admin