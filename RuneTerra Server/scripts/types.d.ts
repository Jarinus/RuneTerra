declare function print(value: any): void

declare interface Player {
    username: string
    packetSender: { sendMessage(message: string): void }
}

declare interface GameObject {
    definition: { name: string }
    position: { x: number, y: number, z: number }
    size: number
}

declare const object: {
    onFirstClick(objectId: number, handler: ({player: Player, gameObject: GameObject}) => void): void
    onSecondClick(objectId: number, handler: ({player: Player, gameObject: GameObject}) => void): void
    onThirdClick(objectId: number, handler: ({player: Player, gameObject: GameObject}) => void): void
    onFourthClick(objectId: number, handler: ({player: Player, gameObject: GameObject}) => void): void
    onFifthClick(objectId: number, handler: ({player: Player, gameObject: GameObject}) => void): void
};

