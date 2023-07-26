// Config
const isOpenClass = "modal-is-open";
const openingClass = "modal-is-opening";
const closingClass = "modal-is-closing";
const animationDuration = 400; // ms
let visibleModal = null;

// Function to handle modal open and close with Alt + S
const toggleModalFromShortcut = () => {
  const modal = document.getElementById("modal-example");
  if (isModalOpen(modal)) {
    closeModal(modal);
  } else {
    openModal(modal);
  }
};

// Toggle modal
const toggleModal = (event) => {
  event.preventDefault();
  const modal = document.getElementById(
      event.currentTarget.getAttribute("data-target")
  );
  typeof modal != "undefined" && modal != null && isModalOpen(modal)
      ? closeModal(modal)
      : openModal(modal);
};

// Is modal open
const isModalOpen = (modal) => {
  return modal.hasAttribute("open") && modal.getAttribute("open") !== "false";
};

// Open modal
const openModal = (modal) => {
  if (isScrollbarVisible()) {
    document.documentElement.style.setProperty(
        "--scrollbar-width",
        `${getScrollbarWidth()}px`
    );
  }
  document.documentElement.classList.add(isOpenClass, openingClass);
  setTimeout(() => {
    visibleModal = modal;
    document.documentElement.classList.remove(openingClass);
  }, animationDuration);
  modal.setAttribute("open", true);

  // Code to disable the Search submit button
  let searchField = modal.querySelector('#search');
  let submitButton = modal.querySelector('button[type="submit"]');

  // Disable the button initially
  submitButton.disabled = true;

  searchField.addEventListener('input', function () {
    submitButton.disabled = !this.value.trim();
  });

  // Set autofocus to focus on the search input field
  searchField.focus();
};


// Close modal
const closeModal = (modal) => {
  let searchButton = modal.querySelector('button[type="submit"]');
  searchButton.setAttribute("aria-busy", "false");
  searchButton.textContent = "Search";

  visibleModal = null;
  document.documentElement.classList.add(closingClass);
  setTimeout(() => {
    document.documentElement.classList.remove(closingClass, isOpenClass);
    document.documentElement.style.removeProperty("--scrollbar-width");
    modal.removeAttribute("open");
  }, animationDuration);
};

// Close with a click outside
document.addEventListener("click", (event) => {
  if (visibleModal != null) {
    const modalContent = visibleModal.querySelector("article");
    const isClickInside = modalContent.contains(event.target);
    !isClickInside && closeModal(visibleModal);
  }
});

// Close with Esc key
document.addEventListener("keydown", (event) => {
  if (event.key === "Escape" && visibleModal != null) {
    closeModal(visibleModal);
  }

  // Check if the Alt key and 's' key are pressed together (event.key is 's', event.altKey is true)
  if (event.key === "s" && event.altKey) {
    event.preventDefault(); // Prevent default behavior of 's' key (e.g., typing 's' in a text input)
    toggleModalFromShortcut(); // Toggle the modal (open/close)
  }
});

// Get scrollbar width
const getScrollbarWidth = () => {
  // Creating invisible container
  const outer = document.createElement("div");
  outer.style.visibility = "scroll";
  outer.style.overflow = "scroll"; // forcing scrollbar to appear
  outer.style.msOverflowStyle = "scrollbar"; // needed for WinJS apps
  document.body.appendChild(outer);

  // Creating inner element and placing it in the container
  const inner = document.createElement("div");
  outer.appendChild(inner);

  // Calculating difference between container's full width and the child width
  const scrollbarWidth = outer.offsetWidth - inner.offsetWidth;

  // Removing temporary elements from the DOM
  outer.parentNode.removeChild(outer);

  return scrollbarWidth;
};

// Is scrollbar visible
const isScrollbarVisible = () => {
  return document.body.scrollHeight > screen.height;
};
