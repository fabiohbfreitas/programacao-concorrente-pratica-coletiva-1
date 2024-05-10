# Programação Concorrente - Atividade Prática Coletiva 1

## Integrantes
- Fábio Henrique Brito de Freitas - UC22100441
- Gabriel Eirado Amaral - UC22200242
- Daniel Ishida

# Sobre o projeto
Sistema que busca simular um sistema de reserva e controle de quartos utilizando Threads em **Java 17**.

## Entidades

- **Room** (Quarto)
- **Guest** (Hóspede)
- **Maid** (Camareira)
- **Receptionist** (Recepcionista)

## Requisitos do projeto

- Hóspedes *devem* ser Threads
- Recepcionistas *devem* ser Threads
- Camareiras *devem* ser Threads
- O hotel *deve* possuir 10 quartos
- O hotel *deve* possuir 5 recepcionistas e 10 camareiras
- *Devem* existir 50 hóspedes

## Funcionamento básico do projeto
Hóspedes chegam ao hotel e aguardam o checkin para um quarto por um recepcionista. Recepcionista verificam se existem hóspedes que desejam fazer checkin, caso sim verificam se há quartos disponíveis e alocam o hóspede (ou a família de 4 ou mais hóspedes), caso não aguardam para quartos ficarem disponíveis. Caso um hóspede (ou família) fique esperando por um quarto por tempo demais, ele faz uma reclamação e vai embora.
Quando alocados em quartos Hospedes podem sair para passear, no caso de famílias todos saem e voltam juntos ao mesmo tempo. Ao fazer checkout as Thread dos hóspedes encerram. Recepcionistas alocam os hóspedes ao mesmo quarto quando eles voltam do passeio se o quarto já foi limpo.  Camareiras limpam o quarto quando os hóspedes saem para passear ou fazem checkout. Quando o último hóspede faz checkout todas as Threads encerram seu trabalho.
