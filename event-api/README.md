# Event api - Based on Vert.x 4
Event api is part of switchin product, giving access to base event. Based on Vert.x 4. 

### Build
- mvn clean package
- mvn docker:build
- mvn docker:push

### Setup Steps(Check infra README.md for infra setup):
- kubectl create namespace event
- kubectl get secret quickstart-es-elastic-user -n elastic -o yaml | sed s/"namespace: elastic"/"namespace: event"/ | sed '/eck.k8s.elastic.co/d' | kubectl apply -n event -f -
- kubectl get secret quickstart-es-http-certs-public -n elastic -o yaml | sed s/"namespace: elastic"/"namespace: event"/ | sed '/eck.k8s.elastic.co/d' | kubectl apply -n event -f -
- helm install event-api ./chart/event-api/ --values ./chart/event-api/values.yaml -n event
- Configs:
    - Append below in file %WINDIR%\System32\drivers\etc\hosts in windows and WSL2 machine in /etc/hosts
      - 127.0.0.1 switchin-event.api
        - event-api - switchin-event.api
- minikube tunnel
- Open kibana.domain.example and PUT /events
- Send traffic to switchin-event.api