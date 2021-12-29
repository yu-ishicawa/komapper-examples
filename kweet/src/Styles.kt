@file:OptIn(KtorExperimentalLocationsAPI::class)

package io.ktor.samples.kweet

import io.ktor.server.application.call
import io.ktor.server.http.content.resolveResource
import io.ktor.server.locations.KtorExperimentalLocationsAPI
import io.ktor.server.locations.Location
import io.ktor.server.locations.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.sessions.get

@Location("/styles/main.css")
class MainCss()

/**
 * Register the styles, [MainCss] route (/styles/main.css)
 */
fun Route.styles() {
    /**
     * On a GET request to the [MainCss] route, it returns the `blog.css` file from the resources.
     *
     * Here we could preprocess or join several CSS/SASS/LESS.
     */
    get<MainCss> {
        call.respond(call.resolveResource("blog.css")!!)
    }
}
