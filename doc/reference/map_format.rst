==========
Map Format
==========

Map to which can be imported are stored in an uncompresed zip file. Containing
a toml file and pictures.

A zip file containing two locations for a Google Maps Mode would look like this:

.. code-block:: guess

	my_map.zip
	|- map.toml
	|- picture1.png
	|- picture2.png

The content of the toml file depends of the map type, currently there are two
different map formats :ref:`ex_google_mode` and :ref:`ex_picture_mode`.

The fields `name` and `type` are the same for every map format.

The fields for the table `meta-data` and the
array table `data-set` can differ depending on the specified map format.

.. _ex_google_mode:

Google Maps Mode
----------------

.. code-block:: guess

	name = "Map Pack 1"
	type = "google-maps"

	[meta-data]
	map = "earth"

	[[data-set]]
	picture = "pic1.png" #path to picture
	latitude = 63.43 #north positive, south negative
	longitude = 10.39 #east positive, west negative
	description = "Third biggest city in Norway" #Optional

	[[data-set]]
	picture = "pic2.png"
	latitude = 52.52
	longitude = 13.4

.. _ex_picture_mode:

Picture Mode
------------

.. code-block:: guess

	name = "Map Pack 2"
	type = "picture"

	[meta-data]
	map = "map.png" #path to map
	dist_x = 35.4
	dist_y = 35.4

	[[data-set]]
	picture = "pic1.png" #path to picture
	x = 378 #Zero being left.
	y = 154 #Zero being top.
	description = "Largest tree in the world"

	[[data-set]]
	picture = "pic2.png"
	x = 293
	y = 37
