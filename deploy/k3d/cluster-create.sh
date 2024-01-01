k3d cluster create --config .\k3d.yml
kubectl create namespace istio-system
helm repo add istio https://istio-release.storage.googleapis.com/charts
helm repo update
kubectl create namespace istio-system
helm install istio-base istio/base -n istio-system --wait
helm install istiod istio/istiod -n istio-system --wait
kubectl label namespace istio-system istio-injection=enabled
helm install istio-ingressgateway istio/gateway -n istio-system --wait
kubectl label namespace default istio-injection=enabled
kubectl create ns crowdproj-comments
kubectl label namespace crowdproj-comments istio-injection=enabled
kubectl create ns keycloak
kubectl label namespace keycloak istio-injection=enabled
