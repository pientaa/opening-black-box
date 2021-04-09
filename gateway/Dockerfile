FROM continuumio/miniconda3
WORKDIR /usr/src/app
COPY ./ ./
RUN conda env create -f environment.yml

# Make RUN commands use the new environment:
SHELL ["conda", "run", "-n", "gateway", "/bin/bash", "-c"]

ENTRYPOINT ["conda", "run", "-n", "gateway", "python", "app.py"]