package chon.group.game.loader.factory;

import java.util.HashMap;
import java.util.Map;

import chon.group.game.core.weapon.ConcreteShot;
import chon.group.game.core.weapon.ConcreteWeapon;
import chon.group.game.core.weapon.Shot;
import chon.group.game.core.weapon.Weapon;
import chon.group.game.loader.config.weapon.WeaponConfig;

public class WeaponFactory {

    public Map<String, Weapon> buildAll(
            Map<String, WeaponConfig> configs,
            Map<String, Shot> shots) {

        Map<String, Weapon> weapons = new HashMap<>();

        for (var entry : configs.entrySet()) {
            String weaponId = entry.getKey();
            WeaponConfig weaponConfig = entry.getValue();

            weapons.put(weaponId, build(weaponId, weaponConfig, shots));
        }

        return weapons;
    }

    public Weapon build(
            String weaponId,
            WeaponConfig config,
            Map<String, Shot> shots) {

        Shot shot = shots.get(config.getShot());

        if (shot == null) {
            throw new IllegalArgumentException(
                    "Shot not found for weapon '" + weaponId + "': " + config.getShot());
        }

        if (!(shot instanceof ConcreteShot concreteShot)) {
            throw new IllegalArgumentException(
                    "Weapon '" + weaponId + "' requires a ConcreteShot, but got: "
                            + shot.getClass().getSimpleName());
        }

        return new ConcreteWeapon(
                config.getOffsetY(),
                concreteShot.getWidth(),
                concreteShot.getHeight(),
                0,
                0,
                config.getEnergyCost(),
                false,
                config.getCooldown(),
                concreteShot);
    }
}