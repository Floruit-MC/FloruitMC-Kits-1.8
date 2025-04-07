# 🎒 Plugin de Kits - Documentação

Este plugin de **Kits** para servidores Minecraft permite criar, visualizar, deletar e distribuir kits personalizados para os jogadores. Ideal para servidores PvP, survival, skywars, e minigames em geral.

---

## 📦 Comandos

| Comando                          | Descrição                                               | Permissão       |
|----------------------------------|-----------------------------------------------------------|-----------------|
| `/kit`                           | Abre o menu com os kits disponíveis                      | ❌ Nenhuma      |
| `/kit view <kit>`               | Visualiza o conteúdo de um kit específico                | ❌ Nenhuma      |
| `/kit create <kit>`            | Cria um novo kit com base no inventário atual           | `kits.create`   |
| `/kit delete <kit>`            | Remove um kit existente                                  | `kits.delete`   |
| `/kit give <jogador> <kit>`    | Entrega um kit a um jogador específico                   | `kits.give`     |

---

## 🔐 Permissões

Adicione as permissões com o plugin de gerenciamento de grupos da sua preferência (ex: **LuckPerms**, **PermissionsEx**, **GroupManager**).

| Permissão     | Descrição                            |
|---------------|----------------------------------------|
| `kits.create` | Permite criar novos kits               |
| `kits.delete` | Permite deletar kits                   |
| `kits.give`   | Permite dar kits para outros jogadores |

---

## 💡 Sugestões de Uso

- Crie kits iniciais para novos jogadores (`/kit create iniciante`)  
- Configure kits de eventos ou desafios semanais  
- Use permissões para criar kits VIP exclusivos  

---

## 🧪 Exemplo Rápido

```bash
# Criar um kit chamado "iniciante"
/kit create iniciante

# Visualizar o kit criado
/kit view iniciante

# Dar o kit para um jogador chamado Alex
/kit give Alex iniciante

# Deletar o kit
/kit delete iniciante
