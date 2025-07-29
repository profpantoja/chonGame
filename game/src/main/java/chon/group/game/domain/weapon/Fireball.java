package chon.group.game.domain.weapon;

import chon.group.game.core.weapon.Shot;

public class Fireball extends Shot {

    /**
     * Construtor atualizado para receber o caminho da imagem.
     * @param posX Posição X inicial
     * @param posY Posição Y inicial
     * @param pathImage Caminho para o sprite do projétil
     * @param direction Direção do projétil ("LEFT" ou "RIGHT")
     */
    public Fireball(int posX, int posY, String pathImage, String direction) {
        super(posX, posY, 32, 32, 10, 1, pathImage, false, 100, direction);
    }
}