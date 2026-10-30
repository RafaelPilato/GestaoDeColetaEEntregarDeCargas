# RotaCerta - Gestão de Coleta e Entrega de Cargas

Sistema em Java puro que simula o setor de operações de uma transportadora, do recebimento da carga até a entrega ao cliente. Desenvolvido como trabalho final da disciplina de **Padrões de Projeto**, aplicando 6 padrões GoF (2 criacionais, 2 estruturais e 2 comportamentais) a regras de negócio reais do transporte rodoviário de cargas.

> **Status:** concluído

## Integrantes

| Integrante | Responsabilidade |
|---|---|
| Rafael | Padrões estruturais (Composite e Adapter), serviços, menu interativo e integração |
| Douglas | Padrões criacionais (Factory Method e Builder) e cálculo de frete |
| Jonas | Padrões comportamentais (State e Observer) e ciclo da entrega |

## Padrões de Projeto aplicados

| Padrão | Categoria | Problema de negócio resolvido |
|---|---|---|
| Factory Method | Criacional | Criar o veículo adequado (VUC, Toco ou Carreta) para cada tipo de operação de transporte, validando capacidade, distância e carga perigosa sem `if/switch` espalhados. |
| Builder | Criacional | Montar a ordem de carga, que tem muitos campos opcionais (seguro, escolta, agendamento), validando os obrigatórios e aplicando seguro e escolta automáticos por regra. |
| Composite | Estrutural | Tratar pallets e volumes da mesma forma, somando peso e valor e identificando carga perigosa em qualquer nível da carga. |
| Adapter | Estrutural | Unificar rastreadores de fornecedores diferentes, cada um com seu formato de resposta, em uma posição padronizada do sistema. |
| State | Comportamental | Controlar o ciclo de status da entrega (Aguardando Coleta, Coletada, Em Trânsito, Retida, Entregue, Devolvida) e impedir transições inválidas. |
| Observer | Comportamental | Avisar automaticamente cliente, financeiro e central de monitoramento a cada mudança de status, sem acoplar a entrega a esses setores. |

## Principais regras de negócio

- O peso de um pallet é a soma dos itens mais 25 kg de tara. Uma carga é perigosa se qualquer item dentro dela for perigoso.
- Carga perigosa torna o seguro obrigatório. Carga acima de R$ 300.000,00 exige escolta. Ambos são adicionados automaticamente.
- Cada operação tem seu veículo e seus limites:

| Operação | Veículo | Capacidade | Distância máxima | Carga perigosa | Custo/km |
|---|---|---|---|---|---|
| Urbana | VUC | 3.000 kg | 100 km | Não | R$ 2,50 |
| Regional | Toco | 6.000 kg | 600 km | Sim | R$ 4,20 |
| Rodoviária | Carreta | 30.000 kg | Sem limite | Sim | R$ 6,80 |

- Frete = distância × custo/km + 0,3% do valor da carga (seguro) + R$ 1.200,00 (escolta) + 20% (carga perigosa).
- O veículo só pode ser alocado ou substituído antes da coleta. A coleta exige veículo alocado.
- Entregue e Devolvida são estados finais: nenhuma ação é permitida depois deles.
- Ao ser entregue, o financeiro emite a fatura. Ao ser devolvida, cobra 50% de frete de retorno. Ao ser retida em fiscalização, o monitoramento emite um alerta.

## Estrutura do projeto

```
src/main/java/br/com/rotacerta/
├── Main.java                  # Menu interativo
├── dominio/                   # Entidades do negócio (Cliente, OrdemCarga, Entrega, carga, veículos)
├── padroes/
│   ├── criacional/            # Factory Method
│   ├── estrutural/            # Adapter
│   └── comportamental/        # State e Observer
├── servico/                   # Casos de uso chamados pelo Main
├── excecao/                   # Exceções de regra de negócio
└── util/                      # Log que exibe os padrões em ação
```

As peças do Composite (`dominio/carga`), o produto do Builder (`OrdemCarga`) e os produtos do Factory Method (`dominio/veiculo`) ficam no domínio, porque são objetos do negócio. Cada classe que participa de um padrão indica seu papel no Javadoc com `@note Papel no Padrão`.

## Tecnologias

- Java 17 (sem frameworks)
- Maven (apenas para a estrutura do projeto, sem dependências)
- IntelliJ IDEA

## Como executar

1. Clone o repositório:
   ```
   git clone https://github.com/RafaelPilato/GestaoDeColetaEEntregarDeCargas.git
   ```
2. Abra a pasta no IntelliJ IDEA (o projeto é reconhecido como Maven pelo `pom.xml`).
3. Execute a classe `br.com.rotacerta.Main`.

## Como usar

O menu principal indica qual padrão cada opção aciona. No terminal, cada padrão em ação aparece com um prefixo, como `[ BUILDER ]` ou `[ STATE ]`.

| Opção | Funcionalidade | Padrão |
|---|---|---|
| 1 | Listar e cadastrar clientes (3 já vêm cadastrados) | - |
| 2 | Montar carga: criar volumes e pallets, colocar e retirar itens de pallets | Composite |
| 3 | Criar ordem de carga com opcionais | Builder |
| 4 | Alocar veículo escolhendo a operação de transporte | Factory Method |
| 5 | Rastrear o veículo escolhendo o fornecedor | Adapter |
| 6 | Atualizar o status da entrega | State + Observer |
| 7 | Ativar e desativar os setores notificados de uma entrega | Observer |
| 8 | Listar entregas e ver detalhes e histórico | - |

### Roteiro de teste sugerido

1. **Opção 2:** crie um pallet e um volume marcado como perigoso, e coloque o volume dentro do pallet. Veja a árvore da carga na opção 4 do submenu.
2. **Opção 3:** crie uma ordem com essa carga e responda "não" ao seguro. O Builder adiciona o seguro automaticamente, porque a carga é perigosa.
3. **Opção 4:** escolha a Operação Urbana. O VUC é criado e recusado, porque não transporta carga perigosa. Escolha a Operação Regional: o Toco é aprovado e o frete é detalhado.
4. **Opção 5:** rastreie o veículo pela SatTrack e depois pela GeoFrota. Os formatos recebidos são diferentes, mas a posição exibida segue o mesmo padrão.
5. **Opção 7:** desative o setor Cliente.
6. **Opção 6:** tente entregar antes de coletar (a ação é barrada). Depois siga o ciclo: coletar, despachar, reter, liberar e entregar. Repare que o Cliente não é mais avisado, o Monitoramento alerta a retenção e o Financeiro emite a fatura na entrega.
7. **Opção 8:** veja os detalhes da entrega e o histórico de status com data e hora.

## Convenções da equipe

- Branch `main` sempre funcionando. Cada integrante trabalhou na sua branch: `feature/estruturais`, `feature/criacionais` e `feature/comportamentais`, com integração final em `feature/integracao`.
- Mensagens de commit no padrão: `feat(builder): valida campos obrigatórios da ordem`.
- Integração na `main` via Pull Request.