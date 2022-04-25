import { Button, Form } from "react-bootstrap";
import { useForm } from "react-hook-form";

export default function SourceForm(props) {
  const { onSubmit } = props;
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  return (
    <Form onSubmit={handleSubmit(onSubmit)}>
      <Form.Group className="mb-3">
        <Form.Label htmlFor="srcOrg">Source Organization</Form.Label>
        <Form.Control
          id="srcOrg"
          type="text"
          isInvalid={Boolean(errors.srcOrg)}
          {...register("srcOrg", { required: "Source Organization is required" })}
        />
        <Form.Control.Feedback type="invalid">
          {errors.srcOrg?.message}
        </Form.Control.Feedback>
      </Form.Group>

      <Form.Group className="mb-3">
        <Form.Label htmlFor="srcRepo">Source Repository</Form.Label>
        <Form.Control
          id="srcRepo"
          type="text"
          isInvalid={Boolean(errors.srcRepo)}
          {...register("srcRepo", { required: "Source Repository is required" })}
        />
        <Form.Control.Feedback type="invalid">
          {errors.srcRepo?.message}
        </Form.Control.Feedback>
      </Form.Group>

      <Form.Group className="mb-3">
        <Form.Label htmlFor="srcProj">Source Project Number</Form.Label>
        <Form.Control
          id="srcProj"
          type="number"
          step="1"
          isInvalid={Boolean(errors.srcProj)}
          {...register("srcProj", { required: "Source Project Number is required" })}
        />
        <Form.Control.Feedback type="invalid">
          {errors.srcProj?.message}
        </Form.Control.Feedback>
      </Form.Group>

      <Button type="submit" data-testid="SourceForm-Submit-Button">Submit Source</Button>
    </Form>
  );
}
