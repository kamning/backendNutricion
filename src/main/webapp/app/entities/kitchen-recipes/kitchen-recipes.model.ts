export interface IKitchenRecipes {
  id?: number;
  name?: string;
  description?: string | null;
  ingredients?: string | null;
  preparation?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  status?: boolean | null;
}

export class KitchenRecipes implements IKitchenRecipes {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public ingredients?: string | null,
    public preparation?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public status?: boolean | null
  ) {
    this.status = this.status ?? false;
  }
}

export function getKitchenRecipesIdentifier(kitchenRecipes: IKitchenRecipes): number | undefined {
  return kitchenRecipes.id;
}
