package com.runeterra.world.entity.combat.method.impl.specials;

import com.runeterra.world.entity.combat.CombatSpecial;
import com.runeterra.world.entity.combat.CombatType;
import com.runeterra.world.entity.combat.hit.QueueableHit;
import com.runeterra.world.entity.combat.method.CombatMethod;
import com.runeterra.world.entity.impl.Character;
import com.runeterra.world.entity.impl.player.Player;
import com.runeterra.world.model.Animation;
import com.runeterra.world.model.Graphic;
import com.runeterra.world.model.GraphicHeight;
import com.runeterra.world.model.Priority;

public class AbyssalWhipCombatMethod implements CombatMethod {

	private static final Animation ANIMATION = new Animation(1658, Priority.HIGH);
	private static final Graphic GRAPHIC = new Graphic(341, GraphicHeight.HIGH, Priority.HIGH);

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
		CombatSpecial.drain(character.getAsPlayer(), CombatSpecial.ABYSSAL_WHIP.getDrainAmount());
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
		if (target.isPlayer()) {
			Player p = (Player) target;
			int totalRunEnergy = p.getRunEnergy() - 25;
			if (totalRunEnergy < 0) {
				totalRunEnergy = 0;
			}
			p.setRunEnergy(totalRunEnergy);
			p.getPacketSender().sendRunEnergy(totalRunEnergy);
			if (totalRunEnergy == 0) {
				p.setRunning(false);
				p.getPacketSender().sendRunStatus();
			}
		}
	}
}