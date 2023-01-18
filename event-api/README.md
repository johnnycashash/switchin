# Event api - Based on Vert.x 4
Event api is part of switchin product, giving access to base event. Based on Vert.x 4. 

### Setup Steps:
- helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
- helm repo add elastic https://helm.elastic.co
- helm repo add grafana https://grafana.github.io/helm-charts
- helm repo update
- curl --proto '=https' --tlsv1.2 -sSfL https://run.linkerd.io/install | sh
- export PATH=$PATH:/home/jagansingh/.linkerd2/bin
- linkerd check --pre
- linkerd install --crds | kubectl apply -f -
- linkerd install --set proxyInit.runAsRoot=true | kubectl apply -f -
- linkerd check
- linkerd viz install | kubectl apply -f -
- linkerd check
- helm install grafana -n grafana --create-namespace grafana/grafana -f https://raw.githubusercontent.com/linkerd/linkerd2/main/grafana/values.yaml
- linkerd viz install --set grafana.url=grafana.grafana:3000 | kubectl apply -f -
- helm install ingress-nginx ingress-nginx/ingress-nginx --values ./switchin/event-api/chart/other/ingress-controller-linkerd-inject.yaml
- kubectl apply -f ./switchin/event-api/chart/other/linkerd-dashboard-ingress.yaml
- helm install elastic-operator elastic/eck-operator -n elastic-system --create-namespace
- kubectl create namespace event
- kubectl apply -f ./switchin/event-api/chart/other/elastic.yaml -n event
- kubectl apply -f ./switchin/event-api/chart/other/kibana.yaml -n event
- kubectl apply -f ./switchin/event-api/chart/other/kibana-ingress.yaml -n event
- kubectl expose service quickstart-kb-http --port=5601 --target-port=5601 --name=quickstart-kb-http-ext --type=NodePort -n event
- echo $(kubectl get secret -n event quickstart-es-elastic-user -o go-template='{{.data.elastic | base64decode}}')
- helm install event-api ./switchin/event-api/chart/event-api/ --values ./switchin/event-api/chart/event-api/values.yaml -n event

- Extra configs:
    - Add below in file %WINDIR%\System32\drivers\etc\hosts

      - 127.0.0.1 kubernetes.docker.internal switchin-event.api kibana.domain.example dashboard.example.com
        - event-api - switchin-event.api
        - kibana - kibana.domain.example elastic/echo $(kubectl get secret -n event quickstart-es-elastic-user -o go-template='{{.data.elastic | base64decode}}')
        - linkerd - dashboard.example.com   admin/admin
    - PUT /events



### Pending:
- cleanup.
- Missing jvm dashboards.
- event bus comm. with other ms.
- side car for these monitoring or linkerd
- jmeter perf.
- visualvm
- test cases.
- mock data in db.
- dockerize app.
- kubernetes files.
- framework on top of ms.
- PUT /events
- auth
- session
- user ms with mapping events
- image storage
- cicd
- mvn wrapper
- helmify all and chart storage
- ssl
- namespace
- vertx jaeger log4j2 configuration
- graalvm image
- ingress
- Flagger
