README: Deploying a Spring Boot Application with Docker and Render
Overview

Testing the Deployed App

you can test this application using the public URL provided by Render:

Base URL:

https://docker-demo-o6yl.onrender.com/


Visiting this URL should display:

Hello, Docker!


Test the /user endpoint:

https://docker-demo-o6yl.onrender.com/user?id=x


Replace x with the user ID you want to query (only from 1 to 10).

This guide explains how to:

Package a Spring Boot application.

Write a Dockerfile.

Build and run a Docker image locally.

Tag and push the Docker image to Docker Hub.

Deploy the image on Render.

Along the way, each step includes an explanation of what happens behind the scenes.

1️⃣ Packaging the Spring Boot App

Before Docker, we need to package our app:

mvn clean package


This generates a JAR file (Java archive) in the target/ directory, e.g., docker-demo-0.0.1-SNAPSHOT.jar.

The JAR contains your compiled Java code and all dependencies.

This is what will be copied into the Docker image.

2️⃣ Writing the Dockerfile

Create a file called Dockerfile in your project root:

# Use official OpenJDK image as base
FROM openjdk:22-jdk-slim

# Add a volume to persist logs (optional)
VOLUME /tmp

# Copy the Spring Boot jar into the container
COPY target/docker-demo-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/app.jar"]


What happens here:

FROM openjdk:22-jdk-slim → Use a lightweight Linux image with JDK 22 installed.

VOLUME /tmp → Optional. Allows Docker to store logs outside the container if needed.

COPY target/... /app.jar → Copies the packaged JAR into the container.

EXPOSE 8080 → Informs Docker and users that the app listens on port 8080.

ENTRYPOINT ["java","-jar","/app.jar"] → Runs the Spring Boot app when the container starts.

3️⃣ Building the Docker Image

Run:

docker build -t docker-demo:1.0 .


docker build → Reads the Dockerfile, executes the instructions, and builds an image.

-t docker-demo:1.0 → Tags the image with the name docker-demo and version 1.0.

. → Build context is the current folder.

Behind the scenes:

Docker executes each instruction in the Dockerfile as a layer.

Layers are cached so future builds are faster.

The final image is a snapshot of the container filesystem ready to run anywhere.

4️⃣ Running the Docker Container Locally
docker run -p 8080:8080 docker-demo:1.0


docker run → Starts a container, a running instance of the image.

-p 8080:8080 → Maps container port 8080 to your local machine’s port 8080.

docker-demo:1.0 → The image to run.

What happens:

Docker creates a container from the image layers.

It sets up networking and mounts volumes (if any).

Runs the ENTRYPOINT (java -jar /app.jar).

Your app is now accessible on http://localhost:8080.

5️⃣ Tagging the Image for Docker Hub
docker tag docker-demo:1.0 tahag17/docker-demo:1.0


Explanation:

docker tag → Gives the image a new name.

Local images can have multiple tags pointing to the same underlying image (same IMAGE ID).

tahag17/docker-demo:1.0 → Format required for Docker Hub (username/repo:tag).

Why not just use latest?

latest is a moving tag and can point to different versions.

Using 1.0 ensures stability, reproducibility, and makes rollbacks easy.

6️⃣ Pushing the Image to Docker Hub
docker login      # Authenticate with Docker Hub
docker push tahag17/docker-demo:1.0


Behind the scenes:

Docker uploads all the layers of the image to your Docker Hub repository.

Layers that already exist on Docker Hub are skipped.

After pushing, the image is available publicly (or privately if your repo is private) and can be pulled anywhere.
7️⃣ Deploying on Render
7.1 What Render Is

Render is a cloud platform that runs web apps, APIs, static sites, and databases.

It can deploy Docker images directly.

Handles networking, HTTPS, and uptime automatically.

Free plan spins down inactive services to save resources.

7.2 Steps to Deploy

Go to Render
→ New → Web Service

Choose Docker → Pull from Docker Registry

Enter your image:

tahag17/docker-demo:1.0


Set environment variables if needed.

Ensure your Spring Boot app uses the PORT variable:

server.port=${PORT:8080}


Click Create Web Service → Render pulls the image and starts your container.

What happens behind the scenes:

Render allocates a cloud server (instance) to your app.

Pulls your Docker image from Docker Hub.

Creates a container and injects environment variables.

Exposes the app via a public URL.

Monitors the container and restarts it if it crashes.

On free plans, instance sleeps after inactivity and wakes up on incoming requests.

7.3 Accessing the App

After deployment, Render gives a URL like:

https://docker-demo-o6yl.onrender.com/


Open it in a browser to see your Spring Boot app running in the cloud.

8️⃣ Notes & Best Practices

Always tag images with versions (1.0, 1.1) for stability.

latest is convenient for development but shouldn’t be used in production.

Free Render instances sleep after inactivity; paid plans keep them always online.

You can connect Render to GitHub for automatic deployments when you push code.

9️⃣ Summary of the Workflow

Package Spring Boot app → .jar

Write Dockerfile → define container environment

Build Docker image → docker build

Run locally → docker run

Tag image for Docker Hub → docker tag

Push to Docker Hub → docker push

Deploy on Render → Pull image, run container, public URL