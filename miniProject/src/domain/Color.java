package domain;

import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Color {
	private int color_id;
	private String color_name;

	public JSONObject colorToJson(Color color) {
		JSONObject jo = new JSONObject();
		jo.put("id", color.getColor_id());
		jo.put("name", color.getColor_name());
		return jo;
	}
	
	public Color jsonToColor(JSONObject jo) {
		Color color = new Color();
		color.setColor_id(jo.getInt("id"));
		color.setColor_name(jo.getString("name"));
		return color;
	}
}
