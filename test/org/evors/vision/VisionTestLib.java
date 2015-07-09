package org.evors.vision;

public abstract class VisionTestLib {

	public static StoredImageSource getImageSource()
	{
		String imgPath = "/Users/michaelgarvie/Documents/eh/camera/arena/";
		FakePositionOrientationSource posSrc = new FakePositionOrientationSource();
		StoredImageSource imgSrc = new StoredImageSource(imgPath, posSrc);
		return imgSrc;
	}

}
