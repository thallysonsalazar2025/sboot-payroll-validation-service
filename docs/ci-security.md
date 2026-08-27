# CI e segurança

O workflow de Pull Request usa Java 17 e Maven Wrapper, sem credenciais de
ambiente. Ele bloqueia o PR por testes, cobertura global, cobertura integral das
linhas Java alteradas, SpotBugs, Gitleaks e dependências com CVSS maior ou igual
a 8.

O baseline medido antes do gate foi 48/76 linhas (63,16%) e 24/34 ramos
(70,59%). Por isso o piso inicial preserva 63% de linhas e 70% de ramos; código
Java alterado deve ter 100% das linhas e ramos executáveis cobertos.

O artefato produzido em `pull_request` corresponde ao merge sintético testado e
serve somente como evidência de validação. Ele não pode ser promovido para
`dev`, `uat` ou `prd`. A promoção exige pipeline confiável pós-merge, SBOM,
proveniência, digest e aprovação segregada.

Este PR não resolve as lacunas funcionais existentes de autenticação,
autorização ou isolamento multitenant e não autoriza deploy.
