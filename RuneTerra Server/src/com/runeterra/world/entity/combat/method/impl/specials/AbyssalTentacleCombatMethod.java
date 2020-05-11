package com.runeterra.world.entity.combat.method.impl.specials;

import com.runeterra.engine.task.impl.CombatPoisonEffect.PoisonType;
import com.runeterra.util.Misc;
import com.runeterra.world.entity.combat.CombatFactory;
import com.runeterra.world.entity.combat.CombatSpecial;
import com.runeterra.world.entity.combat.CombatType;
import com.runeterra.world.entity.combat.hit.QueueableHit;
import com.runeterra.world.entity.combat.method.CombatMethod;
import com.runeterra.world.entity.impl.Character;
import com.runeterra.world.model.Animation;
import com.runeterra.world.model.Graphic;
import com.runeterra.world.model.GraphicHeight;
import com.runeterra.world.model.Priority;

public class AbyssalTentacleCombatMethod implements CombatMethod {

	private static final Animation ANIMATION = new Animation(1658, Priority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(181, GraphicHeight.HIGH, Priority.HIGH);

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
		CombatSpecial.drain(character.getAsPlayer(), CombatSpecial.ABYSSAL_TENTACLE.getDrainAmount());
	}

	@Override
	public float getAttackSpeed(Character character) {
		return character.getBaseAttackSpeed();
	}

	@Override
	public int getAttackDistance(Character character) {
		return 1;
	}

	@Override
	public void startAnimation(Character character) {
		character.performAnimation(ANIMATION);
	}

	@Override
	public void finished(Character character) {

	}

	@Override
	public void handleAfterHitEffects(QueueableHit hit) {
		Character target = hit.getTarget();

		if (target.getHealth() <= 0) {
			return;
		}
		target.performGraphic(GRAPHIC);
		CombatFactory.freeze(target, 10);
		if (Misc.getRandom(100) < 50) {
			CombatFactory.poisonEntity(target, PoisonType.EXTRA);
		}
	}
}