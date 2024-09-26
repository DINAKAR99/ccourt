<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
    <div%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
      <marquee behavior="" direction="right">this is a sample marquee text</marquee>
      <div style="height: 1000px" class="d-flex justify-content-center align-items-center">
        <c:if test="${not empty message}">

          <div class="mt-5 d-flex justify-content-center d-none" id="captchadiv">
            <div class=" text-center border rounded rounded-5 border-1 border-black py-3 w-50 text-white "
              style="background-color: #dc3546db;">
              ${message}</div>
          </div>
        </c:if>
        <h1 class="text-center">HOME PAGE</h1>
      </div>