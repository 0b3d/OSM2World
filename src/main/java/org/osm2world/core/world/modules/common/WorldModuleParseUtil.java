package org.osm2world.core.world.modules.common;

import static org.osm2world.core.util.ValueParseUtil.*;

import java.util.List;

import org.osm2world.core.map_data.data.Tag;
import org.osm2world.core.map_data.data.TagSet;
import org.osm2world.core.util.ValueParseUtil;
import org.osm2world.core.world.creation.WorldModule;

import com.google.common.collect.Lists;

/**
 * utility class that can be used by {@link WorldModule}s
 * to interpret frequently used value formats.
 */
public class WorldModuleParseUtil {

	private WorldModuleParseUtil() { }

	/**
	 * returns the value of the first key that exists,
	 * or the fallback value if none of the keys exists
	 */
	public static final String getValueWithFallback(String fallback, TagSet tags, String... keys) {
		for (String key : keys) {
			if (tags.containsKey(key)) {
				return tags.getValue(key);
			}
		}
		return fallback;
	}

	/**
	 * retrieves width using (in this priority order)
	 * width tag, est_width tag, defaultValue parameter
	 */
	public static final double parseWidth(TagSet tags, double defaultValue) {
		return parseMeasure(tags, defaultValue, "width", "est_width");
	}

	/**
	 * retrieves length using (in this priority order)
	 * length tag, defaultValue parameter
	 */
	public static final double parseLength(TagSet tags, double defaultValue) {
		return parseMeasure(tags, defaultValue, "length");
	}

	/**
	 * retrieves height using (in this priority order)
	 * height tag, building:height tag, est_height tag, defaultValue parameter
	 */
	public static final double parseHeight(TagSet tags, double defaultValue) {
		return parseMeasure(tags, defaultValue, "height", "building:height", "est_height");
	}

	/**
	 * retrieves clearing using (in this priority order)
	 * practical:maxheight tag, maxheight tag, defaultValue parameter
	 */
	public static final double parseClearing(TagSet tags, double defaultValue) {
		return parseMeasure(tags, defaultValue, "maxheight:physical", "maxheight");
	}

	/**
	 * parses the direction tag and returns the direction
	 * (or a default value) as radians
	 */
	public static final double parseDirection(TagSet tags, double defaultValue) {

		Double directionAngle = null;

		if (tags.containsKey("direction")) {
			directionAngle = parseAngle(tags.getValue("direction"));
		}

		if (directionAngle != null) {
			return Math.toRadians(directionAngle);
		} else {
			return defaultValue;
		}

	}

	public static final int parseInt(TagSet tags, int defaultValue, String key) {
		if(tags.containsKey(key)) {
			Double value = parseOsmDecimal(tags.getValue(key), false);
			if (value != null) {
				return(int) (double) value;
			}
		}
		return defaultValue;
	}

	private static final double parseMeasure(TagSet tags, double defaultValue, String... keys) {

		for (String key : keys) {
			if (tags.containsKey(key)) {
				Double value = ValueParseUtil.parseMeasure(tags.getValue(key));
				if (value != null) {
					return value;
				}
			}
		}

		return defaultValue;

	}

	public static final TagSet inheritTags(TagSet ownTags, TagSet parentTags) {

		List<Tag> tags = Lists.newArrayList(ownTags);

		for (Tag tag : parentTags) {
			if (!ownTags.containsKey(tag.key)) {
				tags.add(tag);
			}
		}

		return TagSet.of(tags);

	}

}
