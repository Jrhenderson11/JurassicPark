# Contributions guide

Basically do what you want 

please do everything in a non-master branch and ask to merge it in so it doesnt break stuff

I'd quite like to keep the ai / dinosaur behaviour stuff to myself because its central to everything

There is a TODO list on the main Readme, it is slightly concise / cryptic, here is a more detailed version

The main areas that need improvement:

 - the map generation is quite boring, this is technically done in the JMapper project (https://github.com/Jrhenderson11/Mapper/tree/master/JMapper) but this is the main thing that needs improvement. Rivers especially.

 - there's a bug if you zoom in / move left at the left edge of the screen where it is trying to render 1 column to the left of the map, it would be nice if someone could fix this (the relevant files are GameScene in main package and Renderer in rendering package (it is the GameScene that needs fixing, but Renderer will help u understand)) 

 - Plants! the map generator was designed to make a zoomed out top down landscape, the dark green stuff is meant to be forest. so far there is no code to make plants in realistic places (ie together in clumps instead of scattered randomly)

 - Adding more dinosaurs: This is maybe something for later when more advanced behaviours / traits are in place for dinosaurs but the idea is to have loads of dinosaurs https://en.wikipedia.org/wiki/Dinosaur, http://www.nhm.ac.uk/discover/dino-directory/body-shape/ceratopsian/gallery.html are useful. I think herbivores should be added more first since hunting / defending is a more advanced AI thing, also it makes me sad to see the carnivores kill the other dinosaurs so I'd rather put it off for a while

 - The Astar calculations currently block the game from updating and are quite slow so making it run in a separate thread is a priority
