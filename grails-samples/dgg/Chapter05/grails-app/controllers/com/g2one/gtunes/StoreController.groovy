package com.g2one.gtunes

class StoreController {

    def index = {
    }

    def shop = {
        def genreList = Album.withCriteria {
            projections {
                distinct "genre"
            }
        }

        [top5Albums:Album.list(max:5, sort:"dateCreated", order:"desc"),
         top5Songs:Song.list(max:5, sort:"dateCreated", order:"desc"),
         top5Artists:Artist.list(max:5, sort:"dateCreated", order:"desc"),
         genres:genreList.sort()]		
    }

    def genre = {
        def max =params.max?.toInteger() ?: 10
        if(max > 100) max = 10
        def offset = params.offset?.toInteger() ?: 0

        def total = Album.countByGenre(params.name)
        def albumList = Album.withCriteria {
            eq 'genre', params.name
            projections {
                artist {
                    order 'name'
                }
            }
            maxResults max
            firstResult offset
        }
        return [albums:albumList,
                totalAlbums:total, 
                genre:params.name]
    }
}
