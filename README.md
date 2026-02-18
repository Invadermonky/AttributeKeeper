# Attribute Keeper
Attribute Keeper is a mod that allows users to define attribute, hunger, and XP behavior when a player respawns after dying.

All features can be toggled individually, so you could potentially disable the entire mod if necessary.

---

## ❤️ Attributes
**Q:** What is an attribute?

An attribute is a common way for Minecraft to keep track of entity statistics. These can include health, movement speed, swim speed, reach distance, attack damage, armor and more.

Attribute persistence is the core of Attribute Keeper. In vanilla Minecraft, each time a player is cloned through respawning or some types of teleportation, all attributes are reset to their default values. This means that modifications to a player's health, movement speed, or attack damage all get reset on death. 

While some modifications are intentionally reset, such as modifiers gained via potions or armors, adjustments to the player's base attribute value are also reset, negating any intentional changes.

Attribute Keeper allows users to specify an attribute value and determine whether it persists through death or even decays after respawn.

For example, users could define behavior that causes players to lose 1/2 a heart of health each time they die, down to a minimum of 5 hearts (10 health).

For information on how to configure the Attribute Keeper mod, check out the <a href="https://github.com/Invadermonky/AttributeKeeper/wiki">Attribute Keeper Wiki</a>.

---

## ✨ Experience
Attribute Keeper also includes an Experience Keeper feature, allowing users to define xp behavior on respawn. This includes retaining all experience, losing a set amount of experience, losing a percentage of total experience, and setting a maximum amount of experience that can be kept.

---

## 🍖 Hunger
Attribute Keeper can also define hunger behavior after respawn. Options include:
- Transferring food stats unchanged
- Reducing food stats by a set amount
- Reducing food stats by a percentage of current hunger
- Setting a minimum amount of hunger players will respawn with
- Setting a maximum amount of food players can respawn with


---

## 🛠️ Integration
<ul>
  <li><b>Simple Difficulty / Tough As Nails</b></li>
  <ul>
    <li><b>Temperature Keeper</b> - Body temperature will remain unchanged after respawn.</li>
    <li><b>Thirst Keeper</b> - Functions exactly like the hunger keeper, but for thirst.</li>
    <li><b>Bug Fix</b> - Thirst HUD will now update correctly on login or when changing dimensions.</li>
  </ul>
</ul>

---

## ⚡ Commands
### Reload Command
- `/attributekeeper_reload` -   Reloads the Attribute Keeper attribute json file without a game restart.

### Attribute Commands
* `/playerattribute <target> <attribute> get` - Get the player's current attribute value.
* `/playerattribute <target> <attribute> add <value>` - Add to the player's attribute value.
* `/playerattribute <target> <attribute> set <value>` - Set the player's attribute value.
* `/playerattribute <target> <attribute> mult <value>` - Multiply the player's attribute value.

### Command Examples
* `/playerattribute @p generic.movementSpeed get` - Get the movement speed of the nearest player.
* `/playerattribute @p generic.movementSpeed add 0.1` - Increase the nearest player's movement speed by 0.1.
* `/playerattribute @p generic.movementSpeed set 0.2` - Set the nearest player's movement speed to 0.2.
* `/playerattribute @p generic.movementSpeed mult 2` - Multiply the nearest player's movement speed by 2.

---

## 🚫 Incompatibilities
- Scaling Health - While not directly incompatible, the health modifications added by this mod directly conflict with Attribute Keeper's attribute handling. Attribute Keeper should be compatible so long as you do not define any behavior using the attribute `generic.maxHealth`.

---

## 📜 Credits
- [ACGaming](https://www.curseforge.com/members/acgaming/projects) and [Ski_Z](https://www.curseforge.com/members/ski_z/projects) - Permission to include the [Player Attribute Commands](https://www.curseforge.com/minecraft/mc-mods/player-attribute-commands) command in Attribute Keeper
