import { render, screen } from "@testing-library/react";
import BurgerIcon from "./BurgerIcon";

test('BurgerIcon contains a div with className "burger"', () => {
  render(<BurgerIcon />);
  const appDiv = screen.getByTestId("burger");
  expect(appDiv).toBeInTheDocument();
});
