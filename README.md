# rapid-deploy-cli
CLI Tool developed using Picocli and Java to help deploy applications on Rapid Deploy (https://github.com/SaiUpadhyayula/rapid-deploy).

## Available commands
- rd create-app <app-name> (Create an Application in Rapid Deploy)
- rd apps (List all applications inside Rapid Deploy)

## TODO
- Implement following commands:
    - rd login -u <username> -p <password>
    - rd start-app <app-name>
    - rd stop-app <app-name>
    - rd delete-app <app-name>
    - rd push <app-name>
    - rd logs <app-name>/<service-name>
    - rd create-service <service-name>
    - rd create-service-template <service-template-name>
    - rd start-service <service-name>
    - rd stop-service <service-name>
- Create Native Image and executable using GraalVM.    

