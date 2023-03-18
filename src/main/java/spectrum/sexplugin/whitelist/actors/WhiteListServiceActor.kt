package spectrum.sexplugin.whitelist.actors

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import spectrum.sexplugin.SexPlugin
import spectrum.sexplugin.core.Actor

class WhiteListServiceActor: Actor<Int>() {
    @OptIn(ObsoleteCoroutinesApi::class)
    override fun createActor(): SendChannel<Int> {
        return SexPlugin.scope.actor {  }
    }
}