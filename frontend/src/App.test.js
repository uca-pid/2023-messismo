import { render, screen } from "@testing-library/react";
import App from "./App";

test('App contains a div with className "App"', () => {
  render(<App />);
  const appDiv = screen.getByTestId("app-div");
  expect(appDiv).toBeInTheDocument();
});
