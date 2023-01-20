# Event api - Based on Vert.x 4
Event api is part of switchin product, giving access to base event. Based on Vert.x 4. 

### Setup Steps:
- kubectl create namespace event
- helm install event-api ./switchin/event-api/chart/event-api/ --values ./switchin/event-api/chart/event-api/values.yaml -n event
- Configs:
    - Append below in file %WINDIR%\System32\drivers\etc\hosts in windows and WSL2 machine in /etc/hosts
      - 127.0.0.1 switchin-event.api
        - event-api - switchin-event.api
- minikube tunnel
- Open kibana.domain.example and PUT /events
- Send traffic to switchin-event.api