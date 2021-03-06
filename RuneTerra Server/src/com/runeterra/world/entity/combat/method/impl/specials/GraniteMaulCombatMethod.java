package com.runeterra.world.entity.combat.method.impl.specials;

import com.runeterra.world.entity.combat.CombatSpecial;
import com.runeterra.world.entity.combat.CombatType;
import com.runeterra.world.entity.combat.hit.QueueableHit;
import com.runeterra.world.entity.combat.method.CombatMethod;
import com.runeterra.world.entity.impl.Character;
import com.runeterra.world.model.Animation;
import com.runeterra.world.model.Graphic;
import com.runeterra.world.model.GraphicHeight;
import com.runeterra.world.model.Priority;

/**
 * Granite maul
 * 
 * @author Gabriel Hannason
 */
public class GraniteMaulCombatMethod implements CombatMethod {

	private static final Animation ANIMATION = new Animation(1667, Priority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(340, GraphicHeight.HIGH, Priority.HIGH);

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}

	@Override
	public QueueableHit[] fetchDamage(Character character, Character target) {
		return new QueueableHit[] { new QueueableHit(character, target, this, true, 0) };
	}

	@Override
	public boolean canAttack(Character character, Character target) {
		return true;
	}

	@Override
	public void onQueueAdd(Character character, Character target) {
		CombatSpecial.drain(character.getAsPlayer(), CombatSpecial.GRANITE_MAUL.getDrainAmount());
	}

	@Override
	public float getAttackSpeed(Character character) {
		return 4;
	}

	@Override
	public int getAttackDistance(Character character) {
		return 2;
	}

	@Override
	public void startAnimation(Character character) {
		character.performAnimation(ANIMATION);
		character.performGraphic(GRAPHIC);
	}

	@Override
	public void finished(Character character) {

	}

	@Override
	public void handleAfterHitEffects(QueueableHit hit) {

	}

}
