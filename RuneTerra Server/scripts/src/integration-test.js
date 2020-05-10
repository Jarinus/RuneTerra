object.onFirstClick(18493,  ({player, gameObject}) => {
    const {x, y} = gameObject.position
    player.packetSender.sendMessage(`${player.username} clicked object at [${x}, ${y}]`);
})
