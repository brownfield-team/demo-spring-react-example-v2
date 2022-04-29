import { Button, Form } from "react-bootstrap";
import { useForm } from "react-hook-form";
import ReactJson from "react-json-view";

export default function SourceForm(props) {
  const { onSubmit, source } = props;
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
          isInvalid={Boolean(errors.org)}
          {...register("org", { required: "Source Organization is required" })}
        />
        <Form.Control.Feedback type="invalid">
          {errors.org?.message}
        </Form.Control.Feedback>
      </Form.Group>

      <Form.Group className="mb-3">
        <Form.Label htmlFor="srcRepo">Source Repository</Form.Label>
        <Form.Control
          id="srcRepo"
          type="text"
          isInvalid={Boolean(errors.org)}
          {...register("repo", { required: "Source Repository is required" })}
        />
        <Form.Control.Feedback type="invalid">
          {errors.repo?.message}
        </Form.Control.Feedback>
      </Form.Group>

      <Form.Group className="mb-3">
        <Form.Label htmlFor="srcProj">Source Project Number</Form.Label>
        <Form.Control
          id="srcProj"
          type="number"
          step="1"
          isInvalid={Boolean(errors.proj)}
          {...register("proj", { required: "Source Project Number is required" })}
        />
        <Form.Control.Feedback type="invalid">
          {errors.proj?.message}
        </Form.Control.Feedback>
      </Form.Group>

      <Button type="submit" data-testid="SourceForm-Submit-Button">Submit Source</Button>
      <ReactJson src={source} />
    </Form>
  );
}
