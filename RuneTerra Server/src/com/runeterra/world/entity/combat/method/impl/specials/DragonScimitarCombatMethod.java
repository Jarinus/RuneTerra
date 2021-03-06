package com.runeterra.world.entity.combat.method.impl.specials;

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

public class DragonScimitarCombatMethod implements CombatMethod {

	private static final Animation ANIMATION = new Animation(1872, Priority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(347, GraphicHeight.HIGH, Priority.HIGH);

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
		CombatSpecial.drain(character.getAsPlayer(), CombatSpecial.DRAGON_SCIMITAR.getDrainAmount());
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
		character.performGraphic(GRAPHIC);
	}

	@Override
	public void finished(Character character) {

	}

	@Override
	public void handleAfterHitEffects(QueueableHit hit) {

		if (hit.getTarget().isNpc()) {
			return;
		}

		if (hit.isAccurate()) {
			CombatFactory.disableProtectionPrayers(hit.getTarget().getAsPlayer());
			hit.getAttacker().getAsPlayer().getPacketSender()
					.sendMessage("Your target can no longer use protection prayers.");
		}
	}
}