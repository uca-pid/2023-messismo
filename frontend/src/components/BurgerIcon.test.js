import { render, screen } from "@testing-library/react";
import BurgerIcon from "./BurgerIcon";

test('BurgerIcon contains a div with className "Burger"', () => {
  render(<BurgerIcon />);
  const appDiv = screen.getByTestId("Burger");
  expect(appDiv).toBeInTheDocument();
});
