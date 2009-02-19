<div id="ratingdiv">
    <form id="rating" action="${createLink(action: 'rate', id: parentId)}" method="post" title="${average}">
        <label for="id_rating">Rating:</label>
        <select name="rating" id="id_rating">
            <option value="1">1 - Poor</option>
            <option value="2">2 - Fair</option>
            <option value="3">3 - Good</option>
            <option value="4">4 - Very Good</option>
            <option value="5">5 - Excellent</option>
        </select>
        <input type="submit" value=" Submit rating"/>
    </form>
</div>