#!/bin/bash

if [ -n "$1" ]
then
    $1/graql.sh -k biomed -f ontology.gql
    $1/graql.sh -k biomed -f rules/rules.gql

    $1/migration.sh csv -k biomed -t migrators/hsa-mature-migrator.gql  -i data/hsa-mature.tsv

    $1/migration.sh csv -k biomed -t migrators/hsa-hairpin-migrator.gql -i data/hsa-hairpin.tsv

    $1/migration.sh csv -k biomed -t migrators/cancer-entities.gql -i data/entity_cancer.csv

    $1/migration.sh csv -k biomed -s \| -t migrators/gene-entities.gql -i data/entity_gene.csv

    $1/migration.sh csv -k biomed -s \| -t migrators/unique-cancer-mirna-relations.gql -i data/relation_mir_cancer.csv

    $1/migration.sh csv -k biomed -s \| -t migrators/cancer-mirna-relations-migrator.gql -i data/miRCancerJune2016.tsv

    $1/migration.sh csv -k biomed -s \| -t migrators/hsa-migrator-with-prec.gql -i data/hsa-with-prec.tsv

    $1/migration.sh csv -k biomed -s \| -t migrators/hsa-migrator.gql -i data/hsa.tsv

    $1/migration.sh csv -k biomed -s \| -t migrators/unique-mirna-gene-relations.gql -i data/relation_mir_gene.csv

    $1/migration.sh csv -k biomed -s \| -t migrators/miRTarBase-migrator.gql -i data/miRTarBase_MTI_1000.tsv

    $1/migration.sh csv -k biomed -s \| -t migrators/drugs-migrator.gql -i data/drugs.csv

    $1/migration.sh csv -k biomed -s \| -t migrators/interactions-migrator.gql -i data/interactions.tsv

    cd analytics
    mvn clean package
    java -jar target/degrees-0.14.0-SNAPSHOT-allinone.jar localhost:4567 

else
  echo "Usage: ./loader.sh <Grakn-bin-directory>"
fi
